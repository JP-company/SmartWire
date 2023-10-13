package jpcompany.smartwire.web.machine.dto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class MachineDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MachineDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MachineDto machineDto = (MachineDto) target;

        if (machineDto.getMachineName().length() == 0) {
            errors.rejectValue("machineName", "Required");
        }
        if (machineDto.getSequence() == null) {
            errors.rejectValue("sequence", "Required");
        }
        if (machineDto.getMachineName().length() > 15) {
            errors.rejectValue("machineName", "Range", new Object[]{15}, null);
        }
        if (machineDto.getMachineModel().length() > 15) {
            errors.rejectValue("machineModel", "Range", new Object[]{15}, null);
        }
    }
}
