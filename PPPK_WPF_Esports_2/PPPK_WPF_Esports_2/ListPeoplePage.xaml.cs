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
    /// Interaction logic for ListPeoplePage.xaml
    /// </summary>
    public partial class ListPeoplePage : FramedPage
    {
        public ListPeoplePage(PersonViewModel personViewModel, EsportsTeamViewModel esportsTeamViewModel) : base(personViewModel, esportsTeamViewModel)
        {
            InitializeComponent();
            LvUsers.ItemsSource = personViewModel.People;
        }

        private void BtnEdit_Click(object sender, RoutedEventArgs e)
        {
            if (LvUsers.SelectedItem != null)
            {
                Frame.Navigate(new EditPersonPage(PersonViewModel, EsportsTeamViewModel, LvUsers.SelectedItem as Person)
                {
                    Frame = Frame
                });
            }
        }

        private void BtnDelete_Click(object sender, RoutedEventArgs e)
        {
            if (LvUsers.SelectedItem != null)
            {
                PersonViewModel.People.Remove(LvUsers.SelectedItem as Person);
            }
        }

        private void BtnAdd_Click(object sender, RoutedEventArgs e)
        {
                Frame.Navigate(new EditPersonPage(PersonViewModel, EsportsTeamViewModel)
                {
                    Frame = Frame
                });
        }

        private void BtnEsportsTeams_Click(object sender, RoutedEventArgs e)
        {
            Frame.Navigate(new ListEsportsTeamsPage(PersonViewModel, EsportsTeamViewModel)
            {
                Frame = Frame
            }
            );
        }
    }
}
