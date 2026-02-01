package com.abhishek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class StartApplication {

    @GetMapping("/")
    public String index(final Model model) {
    model.addAttribute("title", "Jay Shankar Kumar – End-to-End CI/CD & GitOps Project");
    model.addAttribute(
        "msg",
        "This project demonstrates a complete end-to-end DevOps implementation designed and built by Jay Shankar Kumar. " +
        "The application is developed using Spring Boot and Maven, containerized with Docker, and continuously integrated using Jenkins. " +
        "Every code change triggers an automated CI pipeline that performs build, testing, static code analysis, and Docker image creation. " +
        "The container image is then pushed to Docker Hub, and Kubernetes deployment manifests are updated automatically in Git. " +
        "Argo CD continuously monitors the Git repository and applies changes to the Kubernetes cluster using a GitOps approach, " +
        "ensuring automated deployment, self-healing, and consistent application state across the environment without manual intervention."
    );
    return "index";
}


    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}
