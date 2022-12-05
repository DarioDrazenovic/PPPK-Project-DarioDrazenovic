using PPPK_WPF_Esports_2.Dal;
using PPPK_WPF_Esports_2.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.ViewModels
{
    public class EsportsTeamViewModel
    {
        public ObservableCollection<EsportsTeam> Teams { get; }

        public EsportsTeamViewModel()
        {
            Teams = new ObservableCollection<EsportsTeam>(RepositoryFactory.GetRepositoryEsports().GetEsportsTeams());
            Teams.CollectionChanged += Teams_CollectionChanged;

        }

        private void Teams_CollectionChanged(object sender, System.Collections.Specialized.NotifyCollectionChangedEventArgs e)
        {
            switch (e.Action)
            {
                case System.Collections.Specialized.NotifyCollectionChangedAction.Add:
                    RepositoryFactory.GetRepositoryEsports().AddEsportsTeam(Teams[e.NewStartingIndex]);
                    break;
                case System.Collections.Specialized.NotifyCollectionChangedAction.Remove:
                    RepositoryFactory.GetRepositoryEsports().DeleteEsportsTeam(e.OldItems.OfType<EsportsTeam>().ToList()[0]);
                    break;
                case System.Collections.Specialized.NotifyCollectionChangedAction.Replace:
                    RepositoryFactory.GetRepositoryEsports().UpdateEsportsTeam(e.NewItems.OfType<EsportsTeam>().ToList()[0]);
                    break;
            }
        }

        public void UpdateEsportsTeam(EsportsTeam team) => Teams[Teams.IndexOf(team)] = team;
    }
}
