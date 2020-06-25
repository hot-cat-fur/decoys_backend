package pesko.orgasms.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Jwt;
import pesko.orgasms.app.domain.models.service.JwtServiceModel;
import pesko.orgasms.app.repository.JwtRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {


    private final JwtRepository jwtRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public JwtServiceImpl(JwtRepository jwtRepository, ModelMapper modelMapper) {
        this.jwtRepository = jwtRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveToken(String token) {

        Jwt jwt= jwtRepository.findByToken(token).orElse(null);

        if(jwt!=null){
            throw new IllegalArgumentException();
        }
        Jwt forSave=new Jwt();

        forSave.setToken(token);
        forSave.setCreatedOn(Date.valueOf(LocalDate.now()));
        forSave.setExpiresOn(Date.valueOf(LocalDate.now().plusDays(1)));
        this.jwtRepository.save(forSave);
    }

    @Override
    public void deleteToken(String token) {

        Jwt jwt= jwtRepository.findByToken(token).orElse(null);

        if(jwt==null){
            throw new IllegalArgumentException();
        }

        this.jwtRepository.delete(jwt);
    }

    @Override
    public List<JwtServiceModel> findAll() {

        return this.jwtRepository.findAll().stream()
                .map(e->this.modelMapper.map(e,JwtServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public JwtServiceModel findByToken(String token) {

        Jwt jwt=this.jwtRepository.findByToken(token).orElse(null);
        if(jwt==null){
            throw new IllegalArgumentException();
        }

        return this.modelMapper.map(jwt,JwtServiceModel.class);
    }
}
