using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;
using VotingSystem;

namespace WPFPieChart
{
    public class AssetClass
    {

        private String myClass;

        public String Class
        {
            get { return myClass; }
            set {
                myClass = value;
                RaisePropertyChangeEvent("Class");
            }
        }

        private double fund;

        public double Fund
        {
            get { return fund; }
            set {
                fund = value;
                //RaisePropertyChangeEvent("Fund");
            }
        }

        private double total;

        public static List<AssetClass> ConstructTestData(Dictionary<string, int> results)
        {
            List<AssetClass> assetClasses = new List<AssetClass>();

            foreach(KeyValuePair<string, int> res in results)
            {
                assetClasses.Add(new AssetClass() { Class = res.Key, Fund = res.Value });
            }

            //assetClasses.Add(new AssetClass(){Class= "Purple team", Fund=73});
            //assetClasses.Add(new AssetClass(){Class= "Red team", Fund=37});
            //assetClasses.Add(new AssetClass(){Class= "Green team", Fund=90});
            //assetClasses.Add(new AssetClass(){Class="Foreign Currency", Fund=16.44, Total=16.44, Benchmark=8.05});
            //assetClasses.Add(new AssetClass(){Class="Stocks; Domestic", Fund=27.57, Total=27.57, Benchmark=38.24});
            //assetClasses.Add(new AssetClass(){Class="Stocks; Foreign", Fund=50.03, Total=50.03, Benchmark=30.93});

            return assetClasses;
        }

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        private void RaisePropertyChangeEvent(String propertyName)
        {
            if (PropertyChanged!=null)
                this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            
        }

        #endregion
    }
}
