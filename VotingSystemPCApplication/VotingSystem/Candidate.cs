using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace VotingSystem
{
    class Candidate
    {
        private BitmapImage userPicture;
        public BitmapImage CandidatePicture
        {
            get
            {
                BitmapImage btm = new BitmapImage(new Uri("/image/candidates.png", UriKind.Relative));
                userPicture = btm;
                return userPicture;
            }
            set { userPicture = value; }
        }
        public string CandidateID { get; set; }
        public string CandidateName { get; set; }
        public string CandidateRating { get; set; }
        public Candidate(string CandidateName)
        {
            this.CandidateName = CandidateName;
        }
    }
}
