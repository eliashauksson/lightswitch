#!/usr/bin

# check if babashka is installed
if ! command -v bb &> /dev/null; then
    echo "babashka is not installed."
    echo "installing babashka..."
    bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)
    echo "babashka was installed."
fi

# copy file to ~/.local/share/lightswitch
mkdir ~/.local/share/lightswitch
cp switch-theme.clj ~/.local/share/lightswitch/.
