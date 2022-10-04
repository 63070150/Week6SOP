package com.example.week6sop;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route(value="index")
public class MainWizardView extends VerticalLayout {
    private int Index = 0;
    private Wizards wizards;
    private Button pre, create, up, del, next;
    private TextField name, doll;
    private RadioButtonGroup<String> gen;
    private Select<String> pos, sch, house;
    public MainWizardView(){
        wizards = new Wizards();
        name = new TextField();
        name.setPlaceholder("Fullname");

        gen = new RadioButtonGroup<>();
        gen.setLabel("Gender");
        gen.setItems("Male", "Female");

        pos = new Select<>();
        pos.setPlaceholder("Position");
        pos.setItems("Student", "Teacher");

        doll = new TextField("Dollars");
        doll.setPlaceholder("$");

        sch = new Select<>();
        sch.setPlaceholder("School");
        sch.setItems("Hogwarts", "Beauxbatons", "Dumstrang");

        house = new Select<>();
        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");

        pre = new Button("<<");
        create = new Button("Create");
        up = new Button("Update");
        del = new Button("Delete");
        next = new Button(">>");
        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(pre, create, up, del, next);
        add(name, gen, pos, doll, sch, house, h1);
        this.refresh();

        pre.addClickListener(e ->{
            Index = Math.max(Index-1, 0);
            this.updateAll();
        });

        create.addClickListener(e ->{
            String n = name.getValue();
            String s = gen.getValue().equals("Male") ? "m" : "f";
            String p = pos.getValue().equals("Student") ? "student" : "teacher";
            int m = Integer.parseInt(doll.getValue());
            String sc = sch.getValue();
            String ho = house.getValue();

            Wizard w = new Wizard(null, s, n, sc, ho, p, m);
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(w), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            Index = wizards.getWizards().size()-1;
//            updateAll();
            refresh();
        });
        up.addClickListener(e -> {
            String id = wizards.getWizards().get(Index).get_id();
            String n = name.getValue();
            String s = gen.getValue().equals("Male") ? "m" : "f";
            String p = pos.getValue().equals("Student") ? "student" : "teacher";
            int m = Integer.parseInt(doll.getValue());
            String sc = sch.getValue();
            String ho = house.getValue();
            Wizard w = new Wizard(id, s, n, sc, ho, p, m);
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .body(Mono.just(w), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            refresh();
        });
        del.addClickListener(e -> {
            String id = wizards.getWizards().get(Index).get_id(); //id for what?
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(wizards.getWizards().get(Index)), Wizard.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            Index = 0;
            refresh();
        });
    }
    private void refresh() {
        System.out.println("refresh()");
        List<Wizard> w = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Wizard>>() {})
                .block();
        System.out.println("size : " + w.size());
        this.wizards.setWizards(w);
        updateAll();
    }
    private void updateAll() {
        System.out.println(Index);
        System.out.println(wizards.getWizards().size());
        pre.setEnabled(!(Index == 0));
        name.setValue(wizards.getWizards().get(Index).getName());
        gen.setValue(wizards.getWizards().get(Index).getSex().equals("m") ? "Male" : "Female");
        pos .setValue(wizards.getWizards().get(Index).getPosition().equals("student") ? "Student" : "Teacher");
        doll.setValue(String.valueOf(wizards.getWizards().get(Index).getMoney()));
        sch.setValue(wizards.getWizards().get(Index).getSchool());
        house.setValue(wizards.getWizards().get(Index).getHouse());
        next.setEnabled(!(Index == wizards.getWizards().size()-1));
    }
}
