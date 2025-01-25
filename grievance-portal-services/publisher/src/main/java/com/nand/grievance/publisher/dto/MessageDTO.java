package com.nand.grievance.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {

    private String postId;
    private PostDTO post;
    private List<PostDTO> responses = new ArrayList<>();

}
