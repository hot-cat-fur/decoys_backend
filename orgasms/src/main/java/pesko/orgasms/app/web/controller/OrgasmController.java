package pesko.orgasms.app.web.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.info.InfoModel;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.domain.models.binding.OrgasmBindingModel;
import pesko.orgasms.app.domain.models.response.OrgasmResponseModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.exceptions.OrgasmAlreadyExistException;
import pesko.orgasms.app.global.GlobalExceptionMessageConstants;
import pesko.orgasms.app.global.MIMETypeConstants;
import pesko.orgasms.app.service.OrgasmService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static pesko.orgasms.app.global.GlobalExceptionMessageConstants.*;

@RestController
@RequestMapping("/orgasm")
public class OrgasmController {

    private final OrgasmService orgasmService;
    private final ModelMapper modelMapper;


    @Autowired
    public OrgasmController(OrgasmService orgasmService, ModelMapper modelMapper) {
        this.orgasmService = orgasmService;
        this.modelMapper = modelMapper;

    }

    @GetMapping("/find/{title}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrgasmResponseModel> findOrgasmByTitle(@PathVariable String title) {



        OrgasmServiceModel orgasmServiceModel = this.orgasmService.findByTitle(title);

        if(orgasmServiceModel==null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok().body(this.modelMapper.map(orgasmServiceModel,OrgasmResponseModel.class));
    }



    @PostMapping(path = "/create/{title}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrgasmResponseModel> addOrgasm(@RequestBody(required = true) MultipartFile file
            , @PathVariable String title, Principal principal) throws IOException {

        String type = file.getContentType();
        if(!this.isMimeTypeValid(type)){
            throw new InvalidMimeTypeException(type, INVALID_MIME_TYPE);
        }

        OrgasmServiceModel orgasmService = this.orgasmService.saveOrgasm(file, title, principal.getName());

        return ResponseEntity.status(201).body(this.modelMapper.map(orgasmService, OrgasmResponseModel.class));
    }




    @PutMapping("/like/{title}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> likeOrgasm(@PathVariable String title, Principal principal) {


        this.orgasmService.likeOrgasm(title, principal.getName());

        return ResponseEntity.ok("Liked");
    }

    @PutMapping("/dislike/{title}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> dislikeOrgasm(@PathVariable String title, Principal principal) {



        this.orgasmService.dislikeOrgasm(title, principal.getName());

        return ResponseEntity.ok("Disliked");
    }

    @GetMapping(path = "/find/liked")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrgasmResponseModel> findLikedOrgasm(Principal principal) {

        OrgasmServiceModel orgasmServiceModel = this.orgasmService.findLikedOrgasm(principal.getName());

        return ResponseEntity
                .ok()
                .body(this.modelMapper.map(orgasmServiceModel, OrgasmResponseModel.class));
    }

    @GetMapping(path = "/find/disliked")
    @PreAuthorize("hasRole('USER')")
    public OrgasmResponseModel findDislikedOrgasm(Principal principal) {

        OrgasmServiceModel orgasmServiceModel = this.orgasmService.findDislikedOrgasm(principal.getName());

        return this.modelMapper.map(orgasmServiceModel, OrgasmResponseModel.class);
    }

    @GetMapping(path = "/find/random")
    @PreAuthorize("hasRole('USER')")
    public OrgasmResponseModel findRandomOrgasm(Principal principal) {

        OrgasmServiceModel orgasmServiceModel = this.orgasmService.findRandomOrgasm(principal.getName());

        return this.modelMapper.map(orgasmServiceModel, OrgasmResponseModel.class);
    }


    @GetMapping(path = "/find/users/all-own")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrgasmResponseModel>> findAllOwnOrgasms(Principal principal) {
        List<OrgasmResponseModel> orgasms = this.orgasmService.findAllUsersOrgasms(principal.getName())
                .stream()
                .map(e -> this.modelMapper.map(e, OrgasmResponseModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(orgasms);
    }

    @GetMapping(path = "/find/users/all-liked")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrgasmResponseModel>> findAllLikedOrgasms(Principal principal) {
        List<OrgasmResponseModel> orgasms = this.orgasmService.findAllUsersLikedOrgasms(principal.getName())
                .stream()
                .map(e -> modelMapper.map(e, OrgasmResponseModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(orgasms);
    }

    @GetMapping(path = "/find/users/all-disliked")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrgasmResponseModel>> findAllDislikedOrgasms(Principal principal) {

        List<OrgasmResponseModel> orgasms = this.orgasmService.findALlUsersDislikedOrgasms(principal.getName())
                .stream()
                .map(e -> modelMapper.map(e, OrgasmResponseModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(orgasms);
    }

    @DeleteMapping(path = "/delete/own/{title}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InfoModel> deleteOwnOrgasm(@PathVariable String title, Principal principal) {

        this.orgasmService.deleteOwnOrgasm(title, principal.getName());

        return ResponseEntity.ok().body(new InfoModel("Deleted"));
    }

    private boolean isMimeTypeValid(String mimeType){
        return MIMETypeConstants.getValidMimeTypes().contains(mimeType);
    }

    @ExceptionHandler({FakeOrgasmException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo orgasmHandlerException(HttpServletRequest request, FakeOrgasmException ex) {


//        ex.printStackTrace();
        return new ErrorInfo(request.getRequestURI(), ex);
    }

    @ExceptionHandler({OrgasmAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo orgasmAlreadyExistHandlerException(HttpServletRequest request, OrgasmAlreadyExistException ex) {

        return new ErrorInfo(request.getRequestURI(), ex);
    }
    @ExceptionHandler({InvalidMimeTypeException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorInfo invalidMIMETypeHandlerException(HttpServletRequest request, InvalidMimeTypeException ex) {

        return new ErrorInfo(request.getRequestURI(), ex);
    }
}
