using PPPK_WPF_Esports_2.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

namespace PPPK_WPF_Esports_2
{
    public class FramedPage : Page
    {
        public FramedPage(PersonViewModel personViewModel, EsportsTeamViewModel esportsTeamViewModel)
        {
            PersonViewModel = personViewModel;
            EsportsTeamViewModel = esportsTeamViewModel;
        }

        public EsportsTeamViewModel EsportsTeamViewModel { get; }
        public PersonViewModel PersonViewModel { get; }
        public Frame Frame { get; set; }
    }
}
