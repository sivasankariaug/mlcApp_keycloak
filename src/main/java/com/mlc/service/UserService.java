package com.mlc.service;

import com.mlc.MlcApplication;
import com.mlc.entity.*;
import com.mlc.exception.MlcException;
import com.mlc.repository.RolesRepository;
import com.mlc.repository.UserMasterRepository;
import com.mlc.repository.UserRolesRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;
    private final UserMasterRepository userMasterDAO;
    private final RolesRepository rolesDAO;
    private final UserRolesRepository userRolesRepository;
    private final String keycloakRealmPath = "/admin/realms/";
    private final String errorMsgPld = "errorMessage";

    @Value("${keycloak.admin.url}")
    private String keycloakAdminUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client.id}")
    private String clientId;

    @Value("${keycloak.client.secret}")
    private String clientSecret;

    public UserService(RestTemplate restTemplate, UserMasterRepository userMasterDAO, RolesRepository rolesDAO, UserRolesRepository userRolesRepository) {
        this.restTemplate = restTemplate;
        this.userMasterDAO = userMasterDAO;
        this.rolesDAO = rolesDAO;
        this.userRolesRepository = userRolesRepository;
    }
    public Response createUser(UserMaster userMaster, String bearerToken) throws MlcException {
        Response responseMessage = new Response();
        if(userMaster != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(bearerToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url= keycloakAdminUrl + keycloakRealmPath + realm + "/users";

            HttpEntity<UserMaster> request = new HttpEntity<>(userMaster, headers);
            try{
                ResponseEntity<Void> response = restTemplate.exchange(
                        url, HttpMethod.POST, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String locationHeader = response.getHeaders().getFirst("Location");
                    String keycloakUserId = extractUserIdFromUrl(locationHeader);
                    if (keycloakUserId != null && !(keycloakUserId.equals(""))){
                        userMaster.setKeycloakUserId(keycloakUserId);
                        userMasterDAO.save(userMaster);
                        responseMessage.setStatusCode(response.getStatusCode().value());
                        responseMessage.setStatusMessage("User created successfully!");
                    }
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed to create user.");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                JSONObject jsonObject = new Gson().fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }
        }
        return responseMessage;
    }
    public String getAccessToken(String username,String password) {
        String message = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String createUserUrl = keycloakAdminUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> requestPaload = new LinkedMultiValueMap<>();
        requestPaload.add("username",username);
        requestPaload.add("password",password);
        requestPaload.add("grant_type","password");
        requestPaload.add("client_id","login-app");
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(requestPaload, headers);

        ResponseEntity<JSONObject> response = restTemplate.exchange(
                createUserUrl, HttpMethod.POST, request, JSONObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject responsePld = response.getBody();
            message = responsePld != null ? (String) responsePld.get("access_token"):"";
            return message;
        } else {
            message= "Failed to create user. Status code: "+response.getStatusCodeValue();
            return message;
        }
    }

    public Response createRole(Roles roles, String accessToken) {
        Response responseMessage = new Response();
        if(roles != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url= keycloakAdminUrl + keycloakRealmPath + realm + "/roles";

            HttpEntity<Roles> request = new HttpEntity<>(roles, headers);
            try{
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.POST, request, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String keyCloakRoleId = getKeyCloakRoleIdByRoleName(url,roles.getName(), headers);
                    roles.setId(keyCloakRoleId);
                    rolesDAO.save(roles);
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Role created successfully!");
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed to create role.");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                JSONObject jsonObject = new Gson().fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }
            catch (Exception ignored){
                responseMessage.setStatusCode(409);
                responseMessage.setStatusMessage("Failed to create role.");
            }

        }
        return responseMessage;
    }

    public ResponseEntity<List<UserMaster>> viewAllUser(){
        List<UserMaster> userMasterList = userMasterDAO.findAll();
        return new ResponseEntity<>(userMasterList,HttpStatus.OK);
    }

    private String extractUserIdFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] parts = path.split("/");
            return parts[parts.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String getKeyCloakRoleIdByRoleName(String url, String roleName, HttpHeaders headers){
        String roleId = "";
        url =url+"/"+roleName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            Gson gson = new Gson();
            JSONObject jsonObject = gson.fromJson(responseBody,JSONObject.class);
            roleId = jsonObject.getAsString("id");
        }
        return roleId;
    }

    public Response assignRoleToUser(UserRoles userRoles, String accessToken) {
        Response responseMessage = new Response();
        if(userRoles != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            UserMaster userMaster = userMasterDAO.getUserMasterByUserName(userRoles.getUserName());
            String url= keycloakAdminUrl + keycloakRealmPath + realm + "/users/"+userMaster.getKeycloakUserId()+"/role-mappings/realm";
            Roles roles = rolesDAO.getRolesByRoleName(userRoles.getRoleName());
            roles.setContainerId(realm);
            List<Roles> rolesList = new ArrayList<>();
            rolesList.add(roles);

            HttpEntity<List<Roles>> request = new HttpEntity<>(rolesList, headers);
            try{
                ResponseEntity<Void> response = restTemplate.exchange(
                        url, HttpMethod.POST, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    userRoles.setIdRoles(roles.getIdroles());
                    userRoles.setIdUserMaster(userMaster.getId());
                    userRolesRepository.save(userRoles);
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Role Mapped successfully!");
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                Gson gson = new Gson();
                JSONObject jsonObject = gson.fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }
        return responseMessage;
    }
    public Response resetUserPassword(UserMaster userMaster, String accessToken) {
        Response responseMessage = new Response();
        if(userMaster != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            Credentials credentials = userMaster.getCredentials().get(0);

            userMaster = userMasterDAO.getUserMasterByUserName(userMaster.getUsername());
            String url= keycloakAdminUrl + keycloakRealmPath + realm + "/users/"+userMaster.getKeycloakUserId()+"/reset-password";
            HttpEntity<Credentials> request = new HttpEntity<>(credentials, headers);
            try{
                ResponseEntity<Void> response = restTemplate.exchange(
                        url, HttpMethod.PUT, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Reset completed!");
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                Gson gson = new Gson();
                JSONObject jsonObject = gson.fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return responseMessage;
    }

    public Response createRealm(Realm realm, String token) {
        Response responseMessage = new Response();
        if(realm != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url= keycloakAdminUrl + keycloakRealmPath;

            HttpEntity<Realm> request = new HttpEntity<>(realm, headers);
            try{
                ResponseEntity<Void> response = restTemplate.exchange(
                        url, HttpMethod.POST, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Realm created successfully!");
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed to create Realm.");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                JSONObject jsonObject = new Gson().fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }
            catch (Exception ignored){
                responseMessage.setStatusCode(409);
                responseMessage.setStatusMessage("Failed to create Realm.");
            }
        }
        return responseMessage;
    }

    public Response createClient(Client client, String token) {
        Response responseMessage = new Response();
        if(realm != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url= keycloakAdminUrl +keycloakRealmPath + realm + "/clients";

            HttpEntity<Client> request = new HttpEntity<>(client, headers);
            try{
                ResponseEntity<Void> response = restTemplate.exchange(
                        url, HttpMethod.POST, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Client created successfully!");
                } else {
                    responseMessage.setStatusCode(response.getStatusCode().value());
                    responseMessage.setStatusMessage("Failed to create Client.");
                }
            }catch (HttpClientErrorException e){
                responseMessage.setStatusCode(e.getStatusCode().value());
                JSONObject jsonObject = new Gson().fromJson(e.getResponseBodyAsString(),JSONObject.class);
                responseMessage.setStatusMessage(jsonObject.getAsString(errorMsgPld));
            }catch (Exception ignored){
                responseMessage.setStatusCode(409);
                responseMessage.setStatusMessage("Failed to create Client.");
            }
        }
        return responseMessage;
    }
}

