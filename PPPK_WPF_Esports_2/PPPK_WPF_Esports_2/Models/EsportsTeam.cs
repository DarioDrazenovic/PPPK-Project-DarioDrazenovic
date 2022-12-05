using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.Models
{
    public class EsportsTeam
    {
        public int IDETeam { get; set; }

        public string TeamName { get; set; }

        public string Country { get; set; }

        public Person Player { get; set; }
    }
}
