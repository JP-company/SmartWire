package jpcompany.smartwire.controller.machine;

import jpcompany.smartwire.controller.member.MemberForm;
import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.service.machine.MachineService;
import jpcompany.smartwire.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MachineController {

    private final MachineService machineService;
    private final MemberService memberService;

    @Autowired
    public MachineController(MachineService machineService, MemberService memberService) {
        this.machineService = machineService;
        this.memberService = memberService;
    }


//    @GetMapping("/account/info/machines")
//    public String machines(Model model) {
//        List<Machine> allMachines = machineService.findAllMachines();
//        model.addAttribute("machines", allMachines);
//        return "member/machine/machineCreateForm";
//    }
    @PostMapping("/account/info/machines")
    public String machineInfo(Member memberForm) {
        return "redirect:/account/info/machines/all?memberId=" + memberForm.getId();
    }

    @GetMapping("/account/info/machines/all")
    public String machinesAll(@RequestParam("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId).get();
        model.addAttribute("memberId", memberId);
        model.addAttribute("companyName", member.getCompanyName());

        List<Machine> allMachines = machineService.findAllMachines(memberId);
        MachineList machineList = new MachineList();
        machineList.setMachines(allMachines);
        model.addAttribute("machineList", machineList);

        return "member/machine/machineCreateForm";
    }
    @PostMapping("/account/info/machines/all")
    public String machineAdd(@RequestParam("memberId") Long memberId, Machine machineForm, Member memberForm) {
        Machine machine = new Machine();
        machine.setMachineName(machineForm.getMachineName());
        machine.setMachineModel(machineForm.getMachineModel());
        machine.setSoftwareVersion(machineForm.getSoftwareVersion());
        machine.setDateManufacture(machineForm.getDateManufacture());

        machineService.addMachine(machine, memberId);
        return "redirect:/account/info/machines/all?memberId=" + memberId;
    }

    @PostMapping("/account/info/machines/all/update")
    public String machineUpdate(@RequestParam("memberId") Long memberId, @ModelAttribute MachineList machineList) {
        List<Machine> machines = machineList.getMachines();
        for (Machine machine : machines) {
            machineService.updateMachine(machine);
        }
        return "redirect:/account/info/machines/all?memberId=" + memberId;
    }
}
