using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
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
    /// Interaction logic for EditVoting.xaml
    /// </summary>
    public partial class EditVoting : Window
    {
        string SDate = "";
        string FDate = "";
        public string result = "";
        DateTime StartDate;
        DateTime FinishDate;
        string VoteNum;
        public EditVoting()
        {
            InitializeComponent();
            VoteNum = null;
            StartDate = DateTime.Today;
            FinishDate = DateTime.Today;
            SDate = DateTime.Today.ToString("yyyy-MM-dd");
            FDate = DateTime.Today.ToString("yyyy-MM-dd");
            StartDisplay.Content = DateTime.Today.ToString("dd/MM/yyyy");
            FinishDisplay.Content = DateTime.Today.ToString("dd/MM/yyyy");
        }

        public EditVoting(Object NewVoting)
        {
            InitializeComponent();
            if (NewVoting is Voting)
            {
                VoteNum = ((Voting)NewVoting).VoteNum;
                VoteName.Text = ((Voting)NewVoting).VoteName;
                VoteDescription.Text = ((Voting)NewVoting).VoteDescription;
                StartHours.Text = ((Voting)NewVoting).Start.Hour.ToString("D2");
                StartMinutes.Text = ((Voting)NewVoting).Start.Minute.ToString("D2");
                FinishHours.Text = ((Voting)NewVoting).Finish.Hour.ToString("D2");
                FinishMinutes.Text = ((Voting)NewVoting).Finish.Minute.ToString("D2");
                StartDate = ((Voting)NewVoting).Start;
                FinishDate = ((Voting)NewVoting).Finish;
                SDate = ((Voting)NewVoting).Start.ToString("yyyy-MM-dd");
                FDate = ((Voting)NewVoting).Finish.ToString("yyyy-MM-dd");
                StartDisplay.Content = ((Voting)NewVoting).Start.ToString("dd/MM/yyyy");
                FinishDisplay.Content = ((Voting)NewVoting).Finish.ToString("dd/MM/yyyy");
            }
        }

        private void EditVotingOnServer(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrWhiteSpace(VoteName.Text) || string.IsNullOrWhiteSpace(VoteDescription.Text) || SDate.Equals("") || FDate.Equals("") || string.IsNullOrWhiteSpace(StartHours.Text) || string.IsNullOrWhiteSpace(StartMinutes.Text) || string.IsNullOrWhiteSpace(FinishHours.Text) || string.IsNullOrWhiteSpace(FinishMinutes.Text))
                MessageBox.Show("Error! One or more fields are missing!");
            else
            {
                try
                {
                    TimeCheck();
                    if(VoteNum == null)
                        result = new Server().AddVoting(VoteName.Text, VoteDescription.Text, SDate + " " + StartHours.Text + ":" + StartMinutes.Text + ":00", FDate + " " + FinishHours.Text + ":" + FinishMinutes.Text + ":00"); //2018-05-29 21:55:00
                    else result = new Server().EditVoting(VoteNum,VoteName.Text, VoteDescription.Text, SDate + " " + StartHours.Text + ":" + StartMinutes.Text + ":00", FDate + " " + FinishHours.Text + ":" + FinishMinutes.Text + ":00");
                    this.Close();
                }
                catch(Exception msg)
                {
                    MessageBox.Show(msg.Message);
                }
            }
        }

        private void SelectStartDate(object sender, RoutedEventArgs e)
        {
            Range window = new Range();
            window.Owner = Window.GetWindow(this);
            window.ShowDialog();
            //window.Owner.ShowDialog(); 
            if (!window.StartDate.Equals(""))
            {
                StartDate = window.StartDate;
                SDate = window.StartDate.ToString("yyyy-MM-dd");
                StartDisplay.Content = window.StartDate.ToString("dd/MM/yyyy");
            }
            if (DateTime.Compare(DateTime.Parse(SDate), DateTime.Parse(FDate)) > 0){
                FinishDate = window.StartDate;
                FDate = SDate;
                FinishDisplay.Content = StartDisplay.Content;
            }      
        }

        private void SelectFinishDate(object sender, RoutedEventArgs e)
        {
            Range window = new Range();
            window.Owner = Window.GetWindow(this);
            window.ShowDialog();
            if (!window.StartDate.Equals(""))
            {
                FinishDate = window.StartDate;
                FDate = window.StartDate.ToString("yyyy-MM-dd");
                FinishDisplay.Content = window.StartDate.ToString("dd/MM/yyyy");
            }
            if (DateTime.Compare(DateTime.Parse(SDate), DateTime.Parse(FDate)) > 0)
            {
                StartDate = window.StartDate;
                SDate = FDate;
                StartDisplay.Content = FinishDisplay.Content;
            }
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            result = "true";
            this.Close();
        }

        private void NamePreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^a-zA-Z0-9 ]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void DescriptionPreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^a-zA-Z0-9 .!?]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void TimePreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^0-9]");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void TimeCheck()
        {
            if(!(StartHours.Text.Length == 2) || !(StartMinutes.Text.Length == 2) || !(FinishHours.Text.Length == 2) || !(FinishMinutes.Text.Length == 2)) throw new Exception("There must be only 2 digits!");
            if (StartDate.Date == DateTime.Today.Date)
                if (System.TimeSpan.Parse(StartHours.Text + ":" + StartMinutes.Text) < DateTime.Now.TimeOfDay) throw new Exception("You can't add past voting!");
            if (FinishDate.Date == StartDate.Date)
                if (System.TimeSpan.Parse(FinishHours.Text + ":" + FinishMinutes.Text) < System.TimeSpan.Parse(StartHours.Text + ":" + StartMinutes.Text)) throw new Exception("The finish time must be bigger than start time");
        }
    }
}
