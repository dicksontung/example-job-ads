const React = require('react');

class UserDropDown extends React.Component {
  constructor(props) {
    super(props);
    this.change = this.change.bind(this)
  }

  change(event) {
    this.props.chooseUserFn(event.target.value)
  }

  render() {
    var users = this.props.users.map(user =>
        <UserOption key={user._links.self.href} user={user}/>
    );

    return (
        <div>
          <select onChange={this.change}>
            {users}
          </select>
        </div>
    )
  }
}

class UserOption extends React.Component {
  render() {
    return <option value={this.props.user.id}>{this.props.user.name}:
      promotion={this.props.user.promotion}</option>
  }
}

module.exports = UserDropDown;