using PPPK_WindowsForms1.Dal;
using PPPK_WindowsForms1.Model;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PPPK_WindowsForms1
{
    public partial class MainForm : Form
    {
        private const string Select = "select";
        private const string Insert = "insert";
        private const string Update = "update";
        private const string Delete = "delete";
        private Database currentDatabase = new Database();

        public MainForm()
        {
            InitializeComponent();
            Init();
        }

        private void Init() => cbDatabases.DataSource = new List<Database>(RepositoryFactory.GetRepository().GetDatabases());

        private void cbDatabases_SelectedIndexChanged(object sender, EventArgs e)
        {
            currentDatabase = cbDatabases.SelectedItem as Database;
        }

        private void btnExecute_Click(object sender, EventArgs e)
        {
            string[] stmts = tbQuery.Text.Trim().Split(';');

            foreach (var stmt in stmts)
            {
                try
                {
                    DataSet ds = RepositoryFactory.GetRepository().CreateDataSet(stmt, currentDatabase);

                    if (stmt.Contains(Select))
                    {

                        dgvResults.DataSource = ds.Tables[0];
                        lbMessage.Text = "(" + ds.Tables[0].Rows.Count + " rows affected )";
                        lbTime.Text = DateTime.Now.ToString();

                    }
                    else if (stmt.Contains(Insert))
                    {

                        dgvResults.DataSource = ds;
                        lbMessage.Text = "Inserted successfully";
                        lbTime.Text = DateTime.Now.ToString();

                    }
                    else if (stmt.Contains(Update))
                    {

                        dgvResults.DataSource = ds;
                        lbMessage.Text = "Updated successfully";
                        lbTime.Text = DateTime.Now.ToString();

                    }
                    else if (stmt.Contains(Delete))
                    {

                        dgvResults.DataSource = ds;
                        lbMessage.Text = "Deleted successfully";
                        lbTime.Text = DateTime.Now.ToString();

                    }
                    else
                    {
                        dgvResults.DataSource = ds;
                        lbMessage.Text = "Completed successfully";
                        lbTime.Text = DateTime.Now.ToString();
                    }
                }
                catch (Exception ex)
                {

                    lbMessage.Text = ex.Message;
                }
            }
        }

        private void MainForm_FormClosed(object sender, FormClosedEventArgs e) => Application.Exit();
    }
}
