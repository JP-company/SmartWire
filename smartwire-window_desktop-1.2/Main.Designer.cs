namespace smartwire_window_desktop
{
    partial class Main
    {
        /// <summary>
        /// 필수 디자이너 변수입니다.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 사용 중인 모든 리소스를 정리합니다.
        /// </summary>
        /// <param name="disposing">관리되는 리소스를 삭제해야 하면 true이고, 그렇지 않으면 false입니다.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form 디자이너에서 생성한 코드

        /// <summary>
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.btnLogin = new System.Windows.Forms.Button();
            this.lblId = new System.Windows.Forms.Label();
            this.IblPassword = new System.Windows.Forms.Label();
            this.tbxId = new System.Windows.Forms.TextBox();
            this.tbxPassword = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.title = new System.Windows.Forms.Label();
            this.MachineSelectView = new System.Windows.Forms.Panel();
            this.MainView = new System.Windows.Forms.Panel();
            this.listView1 = new System.Windows.Forms.ListView();
            this.columnHeader1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader2 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader3 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.LblMachineTitle = new System.Windows.Forms.Label();
            this.LblMachineList = new System.Windows.Forms.Label();
            this.BtnMahcineSave = new System.Windows.Forms.Button();
            this.LIstBoxMahcine = new System.Windows.Forms.ComboBox();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.openFileDialog2 = new System.Windows.Forms.OpenFileDialog();
            this.openFileDialog3 = new System.Windows.Forms.OpenFileDialog();
            this.openFileDialog4 = new System.Windows.Forms.OpenFileDialog();
            this.openFileDialog5 = new System.Windows.Forms.OpenFileDialog();
            this.openFileDialog6 = new System.Windows.Forms.OpenFileDialog();
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.openFileDialog7 = new System.Windows.Forms.OpenFileDialog();
            this.contextMenuStrip2 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.MachineSelectView.SuspendLayout();
            this.MainView.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnLogin
            // 
            this.btnLogin.AllowDrop = true;
            this.btnLogin.Location = new System.Drawing.Point(365, 300);
            this.btnLogin.Name = "btnLogin";
            this.btnLogin.Size = new System.Drawing.Size(75, 23);
            this.btnLogin.TabIndex = 0;
            this.btnLogin.Text = "로그인";
            this.btnLogin.UseVisualStyleBackColor = true;
            this.btnLogin.Click += new System.EventHandler(this.btnLogin_Click);
            // 
            // lblId
            // 
            this.lblId.AutoSize = true;
            this.lblId.Location = new System.Drawing.Point(267, 199);
            this.lblId.Name = "lblId";
            this.lblId.Size = new System.Drawing.Size(41, 12);
            this.lblId.TabIndex = 1;
            this.lblId.Text = "아이디";
            // 
            // IblPassword
            // 
            this.IblPassword.AutoSize = true;
            this.IblPassword.Location = new System.Drawing.Point(255, 248);
            this.IblPassword.Name = "IblPassword";
            this.IblPassword.Size = new System.Drawing.Size(53, 12);
            this.IblPassword.TabIndex = 2;
            this.IblPassword.Text = "비밀번호";
            // 
            // tbxId
            // 
            this.tbxId.Font = new System.Drawing.Font("돋움", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.tbxId.Location = new System.Drawing.Point(323, 192);
            this.tbxId.Name = "tbxId";
            this.tbxId.Size = new System.Drawing.Size(200, 26);
            this.tbxId.TabIndex = 3;
            // 
            // tbxPassword
            // 
            this.tbxPassword.Font = new System.Drawing.Font("돋움", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.tbxPassword.Location = new System.Drawing.Point(323, 241);
            this.tbxPassword.Name = "tbxPassword";
            this.tbxPassword.Size = new System.Drawing.Size(200, 26);
            this.tbxPassword.TabIndex = 4;
            this.tbxPassword.UseSystemPasswordChar = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("휴먼둥근헤드라인", 20.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(318, 138);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(0, 28);
            this.label1.TabIndex = 5;
            // 
            // title
            // 
            this.title.AutoSize = true;
            this.title.Font = new System.Drawing.Font("돋움", 26.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.title.Location = new System.Drawing.Point(279, 131);
            this.title.Name = "title";
            this.title.Size = new System.Drawing.Size(244, 35);
            this.title.TabIndex = 6;
            this.title.Text = "스마트 와이어";
            // 
            // MachineSelectView
            // 
            this.MachineSelectView.Controls.Add(this.MainView);
            this.MachineSelectView.Controls.Add(this.LblMachineTitle);
            this.MachineSelectView.Controls.Add(this.LblMachineList);
            this.MachineSelectView.Controls.Add(this.BtnMahcineSave);
            this.MachineSelectView.Controls.Add(this.LIstBoxMahcine);
            this.MachineSelectView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.MachineSelectView.Location = new System.Drawing.Point(0, 0);
            this.MachineSelectView.Name = "MachineSelectView";
            this.MachineSelectView.Size = new System.Drawing.Size(804, 441);
            this.MachineSelectView.TabIndex = 7;
            this.MachineSelectView.Visible = false;
            // 
            // MainView
            // 
            this.MainView.Controls.Add(this.listView1);
            this.MainView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.MainView.Location = new System.Drawing.Point(0, 0);
            this.MainView.Name = "MainView";
            this.MainView.Size = new System.Drawing.Size(804, 441);
            this.MainView.TabIndex = 4;
            this.MainView.Visible = false;
            // 
            // listView1
            // 
            this.listView1.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2,
            this.columnHeader3});
            this.listView1.HideSelection = false;
            this.listView1.Location = new System.Drawing.Point(0, 0);
            this.listView1.Name = "listView1";
            this.listView1.Size = new System.Drawing.Size(500, 441);
            this.listView1.TabIndex = 0;
            this.listView1.UseCompatibleStateImageBehavior = false;
            this.listView1.View = System.Windows.Forms.View.Details;
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "날짜";
            this.columnHeader1.Width = 100;
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "시간";
            this.columnHeader2.Width = 100;
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "로그";
            this.columnHeader3.Width = 295;
            // 
            // LblMachineTitle
            // 
            this.LblMachineTitle.AutoSize = true;
            this.LblMachineTitle.Location = new System.Drawing.Point(343, 131);
            this.LblMachineTitle.Name = "LblMachineTitle";
            this.LblMachineTitle.Size = new System.Drawing.Size(117, 12);
            this.LblMachineTitle.TabIndex = 3;
            this.LblMachineTitle.Text = "기계를 선택해주세요";
            // 
            // LblMachineList
            // 
            this.LblMachineList.AutoSize = true;
            this.LblMachineList.Location = new System.Drawing.Point(267, 194);
            this.LblMachineList.Name = "LblMachineList";
            this.LblMachineList.Size = new System.Drawing.Size(57, 12);
            this.LblMachineList.TabIndex = 2;
            this.LblMachineList.Text = "기계 목록";
            // 
            // BtnMahcineSave
            // 
            this.BtnMahcineSave.Location = new System.Drawing.Point(355, 259);
            this.BtnMahcineSave.Name = "BtnMahcineSave";
            this.BtnMahcineSave.Size = new System.Drawing.Size(75, 23);
            this.BtnMahcineSave.TabIndex = 1;
            this.BtnMahcineSave.Text = "다음";
            this.BtnMahcineSave.UseVisualStyleBackColor = true;
            this.BtnMahcineSave.Click += new System.EventHandler(this.BtnMahcineSave_Click);
            // 
            // LIstBoxMahcine
            // 
            this.LIstBoxMahcine.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.LIstBoxMahcine.FormattingEnabled = true;
            this.LIstBoxMahcine.Location = new System.Drawing.Point(355, 186);
            this.LIstBoxMahcine.Name = "LIstBoxMahcine";
            this.LIstBoxMahcine.Size = new System.Drawing.Size(182, 20);
            this.LIstBoxMahcine.TabIndex = 0;
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            // 
            // openFileDialog2
            // 
            this.openFileDialog2.FileName = "openFileDialog2";
            // 
            // openFileDialog3
            // 
            this.openFileDialog3.FileName = "openFileDialog3";
            // 
            // openFileDialog4
            // 
            this.openFileDialog4.FileName = "openFileDialog4";
            // 
            // openFileDialog5
            // 
            this.openFileDialog5.FileName = "openFileDialog5";
            // 
            // openFileDialog6
            // 
            this.openFileDialog6.FileName = "openFileDialog6";
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(61, 4);
            // 
            // openFileDialog7
            // 
            this.openFileDialog7.FileName = "openFileDialog7";
            // 
            // contextMenuStrip2
            // 
            this.contextMenuStrip2.Name = "contextMenuStrip2";
            this.contextMenuStrip2.Size = new System.Drawing.Size(61, 4);
            // 
            // Main
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(804, 441);
            this.Controls.Add(this.MachineSelectView);
            this.Controls.Add(this.title);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.tbxPassword);
            this.Controls.Add(this.tbxId);
            this.Controls.Add(this.IblPassword);
            this.Controls.Add(this.lblId);
            this.Controls.Add(this.btnLogin);
            this.Name = "Main";
            this.Text = "Form1";
            this.MachineSelectView.ResumeLayout(false);
            this.MachineSelectView.PerformLayout();
            this.MainView.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnLogin;
        private System.Windows.Forms.Label lblId;
        private System.Windows.Forms.Label IblPassword;
        private System.Windows.Forms.TextBox tbxId;
        private System.Windows.Forms.TextBox tbxPassword;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label title;
        private System.Windows.Forms.Panel MachineSelectView;
        private System.Windows.Forms.Label LblMachineTitle;
        private System.Windows.Forms.Label LblMachineList;
        private System.Windows.Forms.Button BtnMahcineSave;
        private System.Windows.Forms.ComboBox LIstBoxMahcine;
        private System.Windows.Forms.Panel MainView;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.OpenFileDialog openFileDialog2;
        private System.Windows.Forms.OpenFileDialog openFileDialog3;
        private System.Windows.Forms.OpenFileDialog openFileDialog4;
        private System.Windows.Forms.OpenFileDialog openFileDialog5;
        private System.Windows.Forms.OpenFileDialog openFileDialog6;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.OpenFileDialog openFileDialog7;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip2;
        private System.Windows.Forms.ListView listView1;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.ColumnHeader columnHeader3;
    }
}

