package com.mehediFifo.CRM.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private String agentId;
    private String password = "agent12345";
    private String joiningDate;
    private String cellNumber;
    private String email;
    private String fatherName;
    private String motherName;
    private String dob;
    private String sex;
    private String bloodGroup;
    private String presentAddress;
    private String permanentAddress;
    private String homeDistrict;
    private String emergencyContactNumber;
    private String emergencyContactPerson;
    private String religion;
    private String maritalStatus;
    private String highestEducation;
    private String institute;
    private String previousCompany;
    private String bankAcNumber;
    private String nagadAcNumber;
    private String bkashAcNumber;
    private String photo;
    private Boolean status = true;
    @CreationTimestamp
    private LocalDateTime createAt;


}
