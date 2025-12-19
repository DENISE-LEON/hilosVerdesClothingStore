package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.mysql.ProfileDao;
import org.yearup.data.mysql.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;


@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    ProfileDao profileDao;
    UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    //view profiles
    //http://localhost:8080/profile
    @GetMapping
    public Profile getProfileById(Principal principal) {
         try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            Integer userId = user.getId();

            return profileDao.getProfileById(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            Integer userId = user.getId();

            return profileDao.updateProfile(userId, profile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
