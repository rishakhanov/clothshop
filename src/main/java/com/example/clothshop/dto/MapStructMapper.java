package com.example.clothshop.dto;

import com.example.clothshop.entity.*;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {
    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);

    CategoryDTO categoryToCategoryDTO(Category category);
    Category categoryDTOToCategory(CategoryDTO categoryDTO);

    VendorDTO vendorToVendorDTO(Vendor vendor);
    Vendor vendorDTOToVendor(VendorDTO vendorDTO);

    OrderDTO orderToOrderDTO(Orders order);
    Orders orderDTOToOrder(OrderDTO orderDTO);

    OrderProductDTO productToOrderProductDTO(Product product);

    PersonDTO personToPersonDTO(Person person);

    Person PersonDTOToPerson(PersonDTO personDTO);

    Person signupRequestDTOToPerson(SignupRequestDTO signupRequestDTO);

    SignupRequestDTO personToSignupRequestDTO(Person person);

    Person personUpdateDTOToPerson(PersonUpdateDTO personUpdateDTO);

    PersonOrdersDTO orderToPersonOrdersDTO(Orders order);

    DiscountDTO discountToDiscountDTO(Discount discount);

    Discount discountDTOToDiscount(DiscountDTO discountDTO);

    ProductDiscountDTO productToProductDiscountDTO(Product product);

}
