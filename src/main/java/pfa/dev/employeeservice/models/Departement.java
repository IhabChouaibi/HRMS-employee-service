package pfa.dev.employeeservice.models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Departement {

    private Long id;

    private String name;

    private String code;



    private List<Job> jobs;

}
