package jpcompany.smartwire.security.jwt.handler.dto;

import jpcompany.smartwire.web.machine.dto.MachineDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class JwtAuthenticationDto {

    private final List<MachineDto> machineDtoList;
    private final JwtMemberDto jwtMemberDto;

    public JwtAuthenticationDto(JwtMemberDto jwtMemberDto, List<MachineDto> machineDtoList) {
        this.machineDtoList = machineDtoList;
        this.jwtMemberDto = jwtMemberDto;
    }
}
