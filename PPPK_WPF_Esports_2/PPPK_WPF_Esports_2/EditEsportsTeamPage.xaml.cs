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
    /// Interaction logic for EditEsportsTeamPage.xaml
    /// </summary>
    public partial class EditEsportsTeamPage : FramedPage
    {
        private readonly EsportsTeam esportsTeam;

        public EditEsportsTeamPage(PersonViewModel personViewModel, EsportsTeamViewModel esportsTeamViewModel, EsportsTeam esportsTeam1 = null) : base(personViewModel, esportsTeamViewModel)
        {
            InitializeComponent();
            esportsTeam = esportsTeam1 ?? new EsportsTeam();
            DataContext = esportsTeam1;
            CbPlayer.ItemsSource = personViewModel.People;
        }

        private void BtnCommit_Click(object sender, RoutedEventArgs e)
        {
            if (FormValid())
            {
                esportsTeam.TeamName = TbTeamName.Text.Trim();
                esportsTeam.Country = TbCountry.Text.Trim();
                esportsTeam.Player = CbPlayer.SelectedItem as Person;

                if (esportsTeam.IDETeam == 0)
                {
                    EsportsTeamViewModel.Teams.Add(esportsTeam);
                }
                else
                {
                    EsportsTeamViewModel.UpdateEsportsTeam(esportsTeam);
                }
                Frame.NavigationService.GoBack();
            }
        }

        private bool FormValid()
        {
            bool valid = true;
            GridContainer.Children.OfType<TextBox>().ToList().ForEach(e =>
            {
                if (string.IsNullOrEmpty(e.Text.Trim()))

                {
                    e.Background = Brushes.LightCoral;
                    valid = false;
                }
                else
                {
                    e.Background = Brushes.White;
                }
            });

            GridContainer.Children.OfType<ComboBox>().ToList().ForEach(e =>
            {
                if (e.SelectedIndex == -1)
                {
                    string message = "You must choose a player!";

                    MessageBox.Show(message);
                    valid = false;
                }
                else
                {
                    e.Background = Brushes.White;
                }
            });
            return valid;
        }

        private void BtnBack_Click(object sender, RoutedEventArgs e)
        {
            Frame.NavigationService.GoBack();
        }
    }
}
