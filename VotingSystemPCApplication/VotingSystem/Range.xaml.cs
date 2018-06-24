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
using System.Windows.Shapes;

namespace VotingSystem
{
    /// <summary>
    /// Interaction logic for Range.xaml
    /// </summary>
    public partial class Range : Window
    {
        public DateTime StartDate;

        public Range()
        {
            InitializeComponent();
            Start.BlackoutDates.AddDatesInPast();
        }

        private void UserStartDate(object sender, SelectionChangedEventArgs e)
        {
            if (Start.SelectedDate.HasValue)
            {
                SelectedDatesCollection dates = Start.SelectedDates;
                StartDate = dates.First();
                this.Close();
            }
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
