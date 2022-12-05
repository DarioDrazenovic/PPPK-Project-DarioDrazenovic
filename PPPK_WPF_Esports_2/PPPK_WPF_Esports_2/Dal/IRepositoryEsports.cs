using PPPK_WPF_Esports_2.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.Dal
{
    public interface IRepositoryEsports
    {
        void AddEsportsTeam(EsportsTeam team);

        void UpdateEsportsTeam(EsportsTeam team);

        void DeleteEsportsTeam(EsportsTeam team);

        EsportsTeam GetEsportTeam(int idEsportsTeam);

        IList<EsportsTeam> GetEsportsTeams();
    }
}
