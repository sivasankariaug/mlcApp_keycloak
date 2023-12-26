package com.mlc.user;

import com.mlc.entity.Credentials;
import com.mlc.entity.UserMaster;

import java.util.ArrayList;
import java.util.List;

public class UserMockData {
    public UserMaster getUserMaterData(){
       return new UserMaster(1,"XXX 001", "ABC", "xxx@gmail.com", "xxx",true,getListOfCredentials(),"1234567");
    }
    public List<UserMaster> getListOfUserMasterData(){
        List<UserMaster> userMasters = new ArrayList<>();
        userMasters.add(new UserMaster(1,"XXX 001", "ABC", "xxx01@gmail.com", "xxx01",true,getListOfCredentials(),"1234567"));
        userMasters.add(new UserMaster(2,"XXX 002", "DEF", "xxx02@gmail.com", "xxx02",true,getListOfCredentials(),"1234567"));
        userMasters.add(new UserMaster(3,"XXX 003", "GHT", "xxx03@gmail.com", "xxx03",true,getListOfCredentials(),"1234567"));
        return userMasters;
    }

    public List<Credentials> getListOfCredentials(){
        List<Credentials> credentialsList = new ArrayList<>();
        credentialsList.add(new Credentials("password","123",false));
        credentialsList.add(new Credentials("OTP","456789",false));
        return credentialsList;
    }
    public Credentials getCredentials(){
        return new Credentials("password","123",false);
    }


}
