package com.mlc.user;

import com.mlc.entity.UserMaster;
import com.mlc.repository.UserMasterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(locations="classpath:application-test.properties")

public class UserMasterRepositoryTest {
    @Autowired
    private UserMasterRepository userMasterRepository;
    private UserMaster savedUserMaster;

    @Before
    public void testSaveUserMaster(){
        savedUserMaster = userMasterRepository.save(new UserMaster(1,"XXX 001", "ABC", "xxx01@gmail.com", "xxx01",true,null,"1234567"));
    }
    @Test
    public void testFindByName(){
        UserMaster userMaster = userMasterRepository.getUserMasterByUserName("xxx01");
        assertEquals(savedUserMaster.getUsername(),userMaster.getUsername());
    }
}
