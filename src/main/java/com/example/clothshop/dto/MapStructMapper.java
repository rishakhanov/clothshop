package com.example.clothshop.dto;

import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
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

}
