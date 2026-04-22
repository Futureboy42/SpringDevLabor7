/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2025 Thymeleaf (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.examples.springboot3.stsm.mvc.web.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.entities.Feature;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.entities.Row;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.entities.Variety;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.services.SeedStarterService;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.services.VarietyService;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.entities.SeedStarter;
import org.thymeleaf.examples.springboot3.stsm.mvc.business.entities.Type;


@Controller
@SessionAttributes("allFeatures")
public class SeedStarterMngController {


    private VarietyService varietyService;
    private SeedStarterService seedStarterService;
    
    
    
    public SeedStarterMngController() {
        super();
    }


    @Autowired
    public void setVarietyService(final VarietyService varietyService) {
        this.varietyService = varietyService;
    }


    @Autowired
    public void setSeedStarterService(final SeedStarterService seedStarterService) {
        this.seedStarterService = seedStarterService;
    }




    @ModelAttribute("allTypes")
    public List<Type> populateTypes() {
        return Arrays.asList(Type.ALL);
    }
    
    @ModelAttribute("allFeatures")
    public List<Feature> populateFeatures() {
        return Arrays.asList(Feature.ALL);
    }
    
    @ModelAttribute("allVarieties")
    public List<Variety> populateVarieties() {
        return this.varietyService.findAll();
    }
    
    @ModelAttribute("allSeedStarters")
    public List<SeedStarter> populateSeedStarters() {
        return this.seedStarterService.findAll();
    }
    

    /*@RequestMapping({"/","/seedstartermng"})
    public String showSeedstarters(final SeedStarter seedStarter) {
        seedStarter.setDatePlanted(Calendar.getInstance().getTime());
        return "seedstartermng";
    }*/

    @RequestMapping({"/","/seedstartermng"})
    public String showSeedstarters(final SeedStarter seedStarter,
                                   @RequestParam(value = "editRow", required = false) Integer editRow,
                                   Model model) {
        seedStarter.setDatePlanted(Calendar.getInstance().getTime());

        if (editRow != null) {
            model.addAttribute("editRowId", editRow);
        }

        return "seedstartermng";
    }
    
    
    
    @RequestMapping(value="/seedstartermng", params={"save"})
    public String saveSeedstarter(final SeedStarter seedStarter, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "seedstartermng";
        }
        this.seedStarterService.add(seedStarter);
        return "redirect:/seedstartermng";
    }

    @RequestMapping(value="/seedstartermng", params={"delete"})
    public String deleteSeedstarter(@RequestParam int id) {

        this.seedStarterService.delete(id);
        return "redirect:/seedstartermng";
    }
    
    @RequestMapping(value="/seedstartermng", params={"addRow"})
    public String addRow(final SeedStarter seedStarter, final BindingResult bindingResult) {
        seedStarter.getRows().add(new Row());
        return "seedstartermng";
    }

    @RequestMapping(value="/seedstartermng", params={"removeRow"})
    public String removeRow(
            final SeedStarter seedStarter,
            final BindingResult bindingResult,
            @RequestParam(value = "removeRow", required = false) Integer rowId) {
        seedStarter.getRows().remove(rowId.intValue());
        return "seedstartermng";
    }

    /*@RequestMapping(value = "/seedstartermng/edit", params = "id")
    public String editSeedstarter(@RequestParam int id, final org.springframework.ui.Model model) {
        SeedStarter existingSeedStarter = this.seedStarterService.findById(id);
        if (existingSeedStarter == null) {
            return "redirect:/seedstartermng?error=notfound";
        }
        model.addAttribute("seedStarter", existingSeedStarter);
        return "seedstarteredit";
    }*/

    @RequestMapping(value = "/seedstartermng/", params = {"saveEdit"})
    public String saveEditedSeedstarter(final SeedStarter seedStarter, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "seedstartermng";
        }
        this.seedStarterService.update(seedStarter);
        return "redirect:/seedstartermng";
    }


}
