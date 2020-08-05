package pesko.orgasms.app.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pesko.orgasms.app.domain.entities.Orgasm;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.exceptions.OrgasmAlreadyExistException;
import pesko.orgasms.app.repository.OrgasmRepository;
import pesko.orgasms.app.repository.UserRepository;
import pesko.orgasms.app.service.OrgasmService;
import pesko.orgasms.app.utils.ValidatorUtil;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static pesko.orgasms.app.global.GlobalExceptionMessageConstants.*;

@Service
public class OrgasmServiceImpl implements OrgasmService {

    private final OrgasmRepository orgasmRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final UserRepository userRepository;
    private final Random random;
    private final AmazonS3 s3;


    @Value(value = "${aws.bucket}")
    private String bucketName;
    @Value(value = "${aws.file.path}")
    private String filesPath;

    @Value(value = "${aws.p.obj.path}")
    private String publicUrl;

    @Autowired
    public OrgasmServiceImpl(OrgasmRepository orgasmRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, UserRepository userRepository, AmazonS3 s3) {
        this.orgasmRepository = orgasmRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.userRepository = userRepository;
        this.s3 = s3;
        this.random = new Random();
    }


    @Override
    public OrgasmServiceModel saveOrgasm(MultipartFile file, String orgasmTitle, String username) throws IOException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Orgasm oldOrgasm = this.orgasmRepository.findByTitle(orgasmTitle).orElse(null);

        if(oldOrgasm!=null){
            throw new OrgasmAlreadyExistException(ORGASM_ALREADY_EXIST);
        }

        uploadToBucket(file,orgasmTitle);
        Orgasm orgasm=new Orgasm();
        orgasm.setUser(user);
        orgasm.setTitle(orgasmTitle);
        orgasm.setVideoUrl(String.format("%s/%s%s",publicUrl,filesPath,orgasmTitle));

        if(user.getRoles().size()<3){
            orgasm.setPending(true);
        }

        return convertOrgasmToOrgasmServiceModel(this.orgasmRepository.saveAndFlush(orgasm));
    }

    private void uploadToBucket(MultipartFile file,String objectName) throws IOException {

        ObjectMetadata metadata= new ObjectMetadata();
        byte[] bytes= IOUtils.toByteArray(file.getInputStream());

        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(bytes);
       s3.putObject(bucketName,String.format("%s%s",filesPath,objectName),byteArrayInputStream,metadata);

    }


    @Override
    public List<OrgasmServiceModel> findAll() {

        List<OrgasmServiceModel> orgasmServiceModels =
                this.orgasmRepository.findAll()
                        .stream().map(this::convertOrgasmToOrgasmServiceModel).collect(Collectors.toList());

        return orgasmServiceModels;
    }


    @Override
    public void deleteOrgasm(String title) {


        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElseThrow(() -> new FakeOrgasmException(ORGASM_DOESNT_EXIST));

        this.orgasmRepository.deleteById(orgasm.getId());
    }

    @Override
    public OrgasmServiceModel likeOrgasm(String title, String username) {
        this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElseThrow(() -> new FakeOrgasmException(ORGASM_DOESNT_EXIST));

        orgasm.getLikeDislike().put(username, true);

        this.orgasmRepository.save(orgasm);

        return convertOrgasmToOrgasmServiceModel(orgasm);
    }

    @Override
    public OrgasmServiceModel dislikeOrgasm(String title, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElseThrow(() -> new FakeOrgasmException(ORGASM_DOESNT_EXIST));


        orgasm.getLikeDislike().put(username, false);

        this.orgasmRepository.saveAndFlush(orgasm);

        return convertOrgasmToOrgasmServiceModel(orgasm);

    }

    @Override
    public int removeLikeDislikeByUsername(String username) {
        return this.orgasmRepository.deleteLikeDislikeUserKey(username);
    }

    @Override
    public List<OrgasmServiceModel> findAllUsersOrgasms(String username) {
        List<Orgasm> orgList = this.orgasmRepository.findAllByUserUsername(username);

        return orgList.stream()
                .map(this::convertOrgasmToOrgasmServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrgasmServiceModel> findAllUsersLikedOrgasms(String username) {

        List<Orgasm> orgList = this.orgasmRepository.findAllOrgasmsLikedBy(username);

        return orgList.stream().map(this::convertOrgasmToOrgasmServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrgasmServiceModel> findALlUsersDislikedOrgasms(String username) {
        List<Orgasm> orgList = this.orgasmRepository.findAllOrgasmsDislikedBy(username);
        return orgList.stream().map(this::convertOrgasmToOrgasmServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOwnOrgasm(String title, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElseThrow(() -> new FakeOrgasmException(ORGASM_DOESNT_EXIST));

        if (orgasm.getUser() == user) {
            this.orgasmRepository.deleteById(orgasm.getId());
        }else {
            throw new IllegalArgumentException("Inside Job");
        }
    }

    @Override
    public OrgasmServiceModel modifyPending(String title) {
        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElseThrow(() -> new FakeOrgasmException(ORGASM_DOESNT_EXIST));
        orgasm.setPending(!orgasm.isPending());

        return this.modelMapper.map(orgasmRepository.saveAndFlush(orgasm), OrgasmServiceModel.class);
    }

    @Override
    public OrgasmServiceModel findByTitle(String title) {
        Orgasm orgasm = this.orgasmRepository.findByTitle(title).orElse(null);

        return orgasm == null ? null : this.convertOrgasmToOrgasmServiceModel(orgasm);

    }

    @Override
    public OrgasmServiceModel findLikedOrgasm(String username) {


        this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        List<Orgasm> orgasms = this.orgasmRepository.findLiked(username);

        return this.randomOrgasm(orgasms);
    }

    @Override
    public OrgasmServiceModel findDislikedOrgasm(String username) {

        this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        List<Orgasm> orgasms = this.orgasmRepository.findDisliked(username);
        return this.randomOrgasm(orgasms);
    }

    @Override
    public OrgasmServiceModel findRandomOrgasm(String username) {
        this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        List<Orgasm> orgasms = this.orgasmRepository.findRandom(username);

        return this.randomOrgasm(orgasms);
    }



    private OrgasmServiceModel convertOrgasmToOrgasmServiceModel(Orgasm orgasm) {
        return this.modelMapper.map(orgasm, OrgasmServiceModel.class);
    }

    private OrgasmServiceModel randomOrgasm(List<Orgasm> orgasms) {

        if (orgasms.size() < 1) {
            throw new FakeOrgasmException(FAKE_ORGASM);
        }

        int index = this.random.nextInt(orgasms.size());

        OrgasmServiceModel orgasmServiceModel = this.convertOrgasmToOrgasmServiceModel(orgasms.get(index));

        return orgasmServiceModel;
    }

}
