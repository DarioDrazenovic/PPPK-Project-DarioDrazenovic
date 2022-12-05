using PPPK_WPF_Esports_2.Models;
using PPPK_WPF_Esports_2.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace PPPK_WPF_Esports_2
{
    /// <summary>
    /// Interaction logic for ListEsportsTeamsPage.xaml
    /// </summary>
    public partial class ListEsportsTeamsPage : FramedPage
    {
        public ListEsportsTeamsPage(PersonViewModel personViewModel, EsportsTeamViewModel esportsTeamViewModel) : base(personViewModel, esportsTeamViewModel)
        {
            InitializeComponent();
            LvEsportsTeam.ItemsSource = esportsTeamViewModel.Teams;
        }

        private void BtnBack_Click(object sender, RoutedEventArgs e)
        {
            Frame.NavigationService.GoBack();
        }

        private void BtnEdit_Click(object sender, RoutedEventArgs e)
        {
            if (LvEsportsTeam.SelectedItem != null)
            {
                Frame.Navigate(new EditEsportsTeamPage(PersonViewModel, EsportsTeamViewModel, LvEsportsTeam.SelectedItem as EsportsTeam)
                {
                    Frame = Frame
                });
            }
        }

        private void BtnDelete_Click(object sender, RoutedEventArgs e)
        {
            if (LvEsportsTeam.SelectedItem != null)
            {
                EsportsTeamViewModel.Teams.Remove(LvEsportsTeam.SelectedItem as EsportsTeam);

            }
        }

        private void BtnAdd_Click(object sender, RoutedEventArgs e)
        {
            Frame.Navigate(new EditEsportsTeamPage(PersonViewModel, EsportsTeamViewModel)
            {
                Frame = Frame
            });
        }
    }
}
