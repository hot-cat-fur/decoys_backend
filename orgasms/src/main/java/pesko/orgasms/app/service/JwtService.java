package pesko.orgasms.app.service;

import pesko.orgasms.app.domain.entities.Jwt;
import pesko.orgasms.app.domain.models.service.JwtServiceModel;

import java.util.List;

public interface JwtService {

    void saveToken(String token);
    void deleteToken(String token);
    List<JwtServiceModel> findAll();
    JwtServiceModel findByToken(String token);
}
