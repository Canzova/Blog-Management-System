package com.blogManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogListResponse {

    private List<BlogCreateResponseDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isLastPage;
    private Integer totalPages;
    private Long totalElements;

}
