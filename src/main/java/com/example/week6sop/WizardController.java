package com.example.week6sop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService service; //why Service??

    public WizardController(WizardService service) {
        this.service = service;
    }

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public List<Wizard> getWizards(){
        return service.getWizards();
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public Wizard addWizard(@RequestBody Wizard w){
        return service.addWizard(w);
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public Wizard updateWizard(@RequestBody Wizard w){
        return service.updateWizard(w);
    }

    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public boolean deleteWizard(@RequestBody Wizard w){
        return service.deleteWizard(w);
    }//service.deleteWizard(w)
}
