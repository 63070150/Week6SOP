package com.example.week6sop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//??????
@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository) {
        this.repository = repository;
    }

    public List<Wizard> getWizards(){
        System.out.println(repository.findAll());
        return null;
    }

    public Wizard addWizard(Wizard w){
        w.set_id(null);//??
        return repository.save(w);
    }

    public Wizard updateWizard(Wizard w){
        return repository.save(w);
    }

    public boolean deleteWizard(Wizard w){
        repository.deleteById(w.get_id());
        return true;
    }
}
