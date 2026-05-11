package org.relife.config;

import org.relife.entity.Resource;
import org.relife.entity.User;
import org.relife.repository.ResourceRepository;
import org.relife.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataSeeder(UserRepository userRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setFullName("Admin User");
        admin.setDob(LocalDate.of(1990, 1, 15));
        admin.setGender("Other");
        admin.setEmail("admin@relife.org");
        admin.setPhone("555-0100");
        admin.setAddress("1 ReLife Plaza");
        admin.setCity("Mumbai");
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin123"));
        admin.setBio("Platform administrator");
        admin.setRole("ADMIN");
        userRepository.save(admin);

        String pwd = encoder.encode("password123");
        for (Object[] u : List.of(
                new Object[]{"Rahul Sharma", "1985-03-20", "Male", "rahul@example.com", "555-0101", "42 MG Road", "Mumbai", "rahul_sharma", "Looking to help others restart their journey"},
                new Object[]{"Priya Patel", "1992-07-12", "Female", "priya@example.com", "555-0102", "15 Park Street", "Bangalore", "priya_patel", "Career mentor and volunteer"},
                new Object[]{"Amit Kumar", "1988-11-05", "Male", "amit@example.com", "555-0103", "7 Connaught Place", "Delhi", "amit_kumar", "Software professional offering mentorship"}
        )) {
            User user = new User();
            user.setFullName((String) u[0]);
            user.setDob(LocalDate.parse((String) u[1]));
            user.setGender((String) u[2]);
            user.setEmail((String) u[3]);
            user.setPhone((String) u[4]);
            user.setAddress((String) u[5]);
            user.setCity((String) u[6]);
            user.setUsername((String) u[7]);
            user.setPassword(pwd);
            user.setBio((String) u[8]);
            userRepository.save(user);
        }

        List<User> users = userRepository.findAll();
        if (users.size() >= 4) {
            addResource(users.get(1), "Wooden Study Desk", "Good condition desk, perfect for home office", "Household Items", "HOUSEHOLD", "Mumbai", 2, "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=400");
            addResource(users.get(1), "Sofa Set", "3-seater sofa, slight wear but functional", "Furniture", "HOUSEHOLD", "Mumbai", 1, "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400");
            addResource(users.get(2), "Room for Rent", "Furnished room with attached bathroom, female preferred", "Housing", "HOUSING", "Bangalore", 3, "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=400");
            addResource(users.get(2), "Career Mentorship", "1-on-1 career guidance for tech professionals", "Mentorship", "MENTORSHIP", "Bangalore", 2, "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400");
            addResource(users.get(3), "Interview Prep Sessions", "Free mock interviews with detailed feedback", "Skills", "MENTORSHIP", "Delhi", 3, "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=400");
        }
    }

    private void addResource(User u, String title, String desc, String cat, String type, String city, int urg, String img) {
        Resource r = new Resource();
        r.setUserId(u.getUserId());
        r.setTitle(title);
        r.setDescription(desc);
        r.setCategory(cat);
        r.setResourceType(type);
        r.setCity(city);
        r.setUrgency(urg);
        r.setImageUrl(img);
        resourceRepository.save(r);
    }
}
