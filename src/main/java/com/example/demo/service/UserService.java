package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long id) throws  UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found!");
        }
        userRepository.deleteById(id);
    }

    public UserDTO getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
        return UserMapper.toUserDTO(user);
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("User with name " + username + " not found!"));
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(UserMapper.toUserDTO(user));
        }
        return usersDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return List.of(authority);
    }
}
