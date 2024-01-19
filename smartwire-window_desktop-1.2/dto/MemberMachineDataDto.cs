using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace smartwire_window_desktop.dto
{
    internal class MemberMachineDataDto
    {
        public JwtMemberDto JwtMemberDto { get; set; }
        public List<MachineDto> MachineDtoList { get; set; }
    }
}
