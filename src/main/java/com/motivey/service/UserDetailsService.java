package com.motivey.service;

import com.motivey.model.User;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


public interface UserDetailsService {


    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}

