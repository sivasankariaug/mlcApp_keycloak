package com.mlc.user;

import com.mlc.entity.Credentials;
import com.mlc.entity.UserMaster;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMasterTest {
    private UserMockData testResource;
    @Before
    public void setUp(){
        testResource = new UserMockData();
    }
    @Test
    public void testUserMasterGetterSetterValues(){
        UserMaster getterValue = testResource.getUserMaterData();

        UserMaster setterValue = new UserMaster();
        setterValue.setId(getterValue.getId());
        setterValue.setFirstName(getterValue.getFirstName());
        setterValue.setLastName(getterValue.getLastName());
        setterValue.setUsername(getterValue.getUsername());
        setterValue.setKeycloakUserId(getterValue.getKeycloakUserId());
        setterValue.setEmail(getterValue.getEmail());
        setterValue.setEnabled(getterValue.isEnabled());
        setterValue.setCredentials(getterValue.getCredentials());

        assertEquals(getterValue.toString(),setterValue.toString());
        EqualsVerifier.simple().forClass(UserMaster.class)
                .withPrefabValues(Credentials.class, testResource.getCredentials(), testResource.getListOfCredentials().get(1))
                .verify();
    }

}
