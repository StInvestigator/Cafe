package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String position;
}
