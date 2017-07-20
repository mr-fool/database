# Git

Git is a command line source control tool used to document changes made to a project.

See: https://git-scm.com/course/svn.html

There are many applications you can use for git on both linux and windows. GitKraken, Smartgit, etc.
Command line git works just as well.

## Creating an SSH Key

Your machine will need an SSH key for push/pull access to the repository.

### Windows

Begin by downloading PuTTYgen:
> http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html

Create a .ssh folder in your user directory

>Open up a file browser, and navigate to your users directory, C:\users\YOURUSER
>
> In the top directory bar (ie where it says C:\users\YOURUSER) erase the contents and type 'cmd' to open a prompt
>
> Type:
>
> mkdir .ssh
>
> (Windows doesn't like to create folders with . infront through the conventional rightclick -> new folder)

- Run PuTTYgen
- Click Generate
- Move the mouse around a bit
- Choose a password
- Conversions - > Export Open SSH key
- Navigate to the .ssh folder and save the file as 'id_rsa'
- Copy the text in 'Key: Public key for pasting into OpenSSH authorized_keys file'
- Paste into a text editor, and save the file as id_rsa.pub in the .ssh folder

### Linux

Open a terminal and type:
> ssh-keygen -t rsa -b 4096 -C "your_email@here.com"

Enter a password when prompted

Press enter to select the default save location (/home/pi/.ssh/id_rsa.pub)

## GitLab SSH Key Authorization

Login to GitLab, and go to:
> https://gitlab.com/profile/keys

Click Add

Paste the contents of the id_rsa.pub you generated when creating an SSH key

# Cloning the repository

## Windows

Install your selected Git client (or command line git). I will outline SmartGit
and GitKraken setup steps.

Using GitKraken:

- Download & install GitKraken
- Settings->Git Config->Setup Name and Email
- Authentication->Uncheck 'Use local SSH agent' (ensure default folder is C:\users\YOUR_USER\.ssh\)
- File->Clone Repo
- Choose a folder and clone git@gitlab.com:KyleGG/database.git

Using SmartGit:

- Download & Install SmartGit
- Repository->Clone
- Choose a folder and clone git@gitlab.com:KyleGG/database.git

## Linux

In a terminal, make a directory for the workspace
> mkdir ~/workspaces
>
> cd workspaces
>
> git clone git@gitlab.com:KyleGG/capstone.git

