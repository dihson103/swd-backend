package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.course.CourseResponse;
import com.dihson103.onlinelearning.dto.enroll.EnrollResponse;
import com.dihson103.onlinelearning.entities.*;
import com.dihson103.onlinelearning.repositories.CourseRepository;
import com.dihson103.onlinelearning.repositories.EnrollRepository;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.IEnrollService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollService implements IEnrollService {

    private final EnrollRepository enrollRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final FiltersSpecification<Enroll> filtersSpecification;

    private Double getPriceWhenSale(Course course, LocalDateTime now) {
        Discount validDiscount = course.getDiscounts()
                .stream()
                .filter(discount -> discount.getDateFrom().isBefore(now) && discount.getDateTo().isAfter(now))
                .findFirst()
                .orElse(null);

        if (validDiscount != null) {
            return course.getPrice() * (1 - validDiscount.getDiscount() / 100.0);
        }
        return course.getPrice();
    }

    @Override
    public void enrollCourse(String username, Integer courseId) {
        UserEntity user = userRepository.findByUsernameAndStatusIsTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("Can not find user."));
        EnrollKey enrollKey = EnrollKey.builder()
                .courseId(courseId)
                .userId(user.getId())
                .build();
        Boolean isUserEnrolled = enrollRepository.findById(enrollKey).isPresent();
        if(isUserEnrolled){
            throw new IllegalArgumentException("User are already enrolled this course.");
        }
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id: " + courseId));
        LocalDateTime now = LocalDateTime.now();
        Enroll enroll = Enroll.builder()
                .id(enrollKey)
                .course(course)
                .user(user)
                .enrollDate(now)
                .price(getPriceWhenSale(course, now))
                .build();
        enrollRepository.save(enroll);
    }

    @Override
    public List<EnrollResponse> getEnrollListByUser(String username) {
        List<Enroll> enrolls = enrollRepository.getListEnrollByUsername(username);
        return enrolls.stream()
                .map(this::apply)
                .toList();
    }

    @Override
    public List<EnrollResponse> searchEnrolledCourses(String username, String searchValue) {
        List<Enroll> enrolls = enrollRepository.getEnrollsByUsernameAndSearch(username, searchValue);
        return enrolls.stream()
                .map(this::apply)
                .toList();
    }

    private EnrollResponse apply(Enroll enroll) {
        return EnrollResponse.builder()
                .courseResponse(modelMapper.map(enroll.getCourse(), CourseResponse.class))
                .enrollDate(enroll.getEnrollDate())
                .price(enroll.getPrice())
                .build();
    }
}
