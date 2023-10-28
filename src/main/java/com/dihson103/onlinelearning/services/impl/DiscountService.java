package com.dihson103.onlinelearning.services.impl;

import com.dihson103.onlinelearning.dto.discount.DiscountRequest;
import com.dihson103.onlinelearning.dto.discount.DiscountResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.entities.Course;
import com.dihson103.onlinelearning.entities.Discount;
import com.dihson103.onlinelearning.repositories.CourseRepository;
import com.dihson103.onlinelearning.repositories.DiscountRepository;
import com.dihson103.onlinelearning.services.FiltersSpecification;
import com.dihson103.onlinelearning.services.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService implements IDiscountService {

    private final DiscountRepository discountRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final FiltersSpecification<Discount> filtersSpecification;

    private void checkDateOfDiscount(DiscountRequest discountRequest){
        if(!discountRequest.isFromDateValid()){
            throw new IllegalArgumentException("Date from should be more than current date.");
        }
        if(!discountRequest.isDateToValid()){
            throw new IllegalArgumentException("Date to should be more than date from.");
        }
    }

    private Course findAndCheckCourse(DiscountRequest discountRequest){
        Integer courseId = discountRequest.getCourseId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find course has id: " + courseId));
        if(!course.getStatus()){
            throw new IllegalArgumentException("Please active this course after set discount.");
        }
        return course;
    }

    private Boolean isCourseHasDiscountActive(Course course, DiscountRequest discountRequest){
        return course.getDiscounts().stream()
                .anyMatch(discount -> discount.getDateTo().compareTo(discountRequest.getDateFrom()) > 0);
    }

    @Override
    public void createNewDiscount(DiscountRequest discountRequest) {
        checkDateOfDiscount(discountRequest);
        Course course = findAndCheckCourse(discountRequest);
        if(isCourseHasDiscountActive(course, discountRequest)){
            throw new IllegalArgumentException("This course already has discount active.");
        }
        Discount discount = modelMapper.map(discountRequest, Discount.class);
        discount.setCourse(course);
        discountRepository.save(discount);
    }

    private void checkDiscountActive(Discount discount){
        if(discount.getDateFrom().compareTo(LocalDateTime.now()) <= 0){
            throw new IllegalArgumentException("This discount was active. Can not update or delete this discount.");
        }
    }

    @Override
    public void updateDiscount(Integer discountId, DiscountRequest discountRequest) {
        checkDateOfDiscount(discountRequest);
        Course course = findAndCheckCourse(discountRequest);
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find discount has id: " + discountId));
        checkDiscountActive(discount);
        discount.setDateFrom(discountRequest.getDateFrom());
        discount.setDateTo(discountRequest.getDateTo());
        discount.setDiscount(discount.getDiscount());
        discount.setCourse(course);
        discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Integer discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find discount has id: " + discountId));
        checkDiscountActive(discount);
        discountRepository.delete(discount);
    }

    @Override
    public List<DiscountResponse> filterDiscounts(FilterRequestDto filterRequestDto) {
        Specification specification = filtersSpecification.getSpecification(
                filterRequestDto.getSearchRequestDto(),
                filterRequestDto.getGlobalOperator()
        );
        List<Discount> discounts = discountRepository.findAll(specification);
        return discounts.stream()
                .map(discount -> modelMapper.map(discount, DiscountResponse.class))
                .toList();
    }

}
