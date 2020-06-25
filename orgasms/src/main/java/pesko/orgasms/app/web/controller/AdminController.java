package pesko.orgasms.app.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pesko.orgasms.app.domain.models.view.AdminUrlViewModel;

import java.util.Arrays;

@RestController
public class AdminController {

    @Value("${cloudinary.video.app}")
   private String url;

    @GetMapping(value = "/admin/check")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUrlViewModel areYouAdmin(){


        return new AdminUrlViewModel(url);
    }
}
