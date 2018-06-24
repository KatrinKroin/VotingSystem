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
    /// Interaction logic for AssignedCandidates.xaml
    /// </summary>
    public partial class AssignedCandidates : Window
    {
        List<Candidate> Candidates;
        string VoteNum;
        public string result = "false";
        public AssignedCandidates(string VoteNum)
        {
            InitializeComponent();
            this.VoteNum = VoteNum;
        }

        private void CandidatesLoading(object sender, RoutedEventArgs e)
        {
            try
            {
                UserSingleton UserAdmin = UserSingleton.GetInstance();
                string UserAdminID = UserAdmin.GetUserID();
                Candidates = new Server().SetCandidates(VoteNum);
                if (Candidates != null) CandudatesList.ItemsSource = Candidates;
                CandudatesList.SelectedIndex = 0;
            }
            catch (Exception msg)
            {
                LoginWindow window = new LoginWindow();
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                CandidatesLoading(sender, e);
            }
        }

        private void UpdateCandidates(object sender, RoutedEventArgs e)
        {
            if (CandudatesList.Items.Count < 2)
                MessageBox.Show("Error! The amount of candidates should be at least two!");
            else
            {
                result = new Server().UpdateCandidates(VoteNum, Candidates);
                this.Close();
            }
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            result = "true";
            this.Close();
        }

        private void AddCandidate(object sender, RoutedEventArgs e)
        {
            if(string.IsNullOrEmpty(CandidateName.Text))
                MessageBox.Show("Error! Candidate name is missing!");
            else if(Candidates.Exists(x => x.CandidateName == CandidateName.Text))
                MessageBox.Show("Error! Candidate is already exists!");
            else
            {
                Candidates.Add(new Candidate(CandidateName.Text));
                ReloadCandidateList();
            }
        }

        private void RemoveCandidate(object sender, RoutedEventArgs e)
        {
            if (CandudatesList.SelectedIndex != -1)
            {
                Candidates.RemoveAt(CandudatesList.SelectedIndex);
                ReloadCandidateList();
            }
        }

        private void ReloadCandidateList()
        {
            CandudatesList.ItemsSource = null;
            CandudatesList.ItemsSource = Candidates;
            CandudatesList.SelectedIndex = 0;
        }
    }
}
