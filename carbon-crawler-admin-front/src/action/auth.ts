import * as SharedAction from './shared'
import {AuthInfo} from "../shared";
import {SignUpValues} from "../component/auth";

export class SignUp extends SharedAction.FsaActionCreator<Types.AUTH_SIGN_UP, SignUpValues> {
  constructor(signUpInfo: SignUpValues) {
    super(Types.AUTH_SIGN_UP, signUpInfo);
  }
}

export class SignIn extends SharedAction.FsaActionCreator<Types.AUTH_SIGN_IN, AuthInfo> {
  constructor(auth: AuthInfo) {
    super(Types.AUTH_SIGN_IN, auth)
  }
}

export class SignOut extends SharedAction.FsaActionCreator<Types.AUTH_SIGN_OUT> {
  constructor() {
    super(Types.AUTH_SIGN_OUT, {})
  }
}

export class SignInSuccess extends SharedAction.FsaActionCreator<Types.AUTH_SIGN_IN_SUCCESS> {
  constructor() {
    super(Types.AUTH_SIGN_IN_SUCCESS, {})
  }
}

export class SignOutSuccess extends SharedAction.FsaActionCreator<Types.AUTH_SIGN_OUT_SUCCESS> {
  constructor() {
    super(Types.AUTH_SIGN_OUT_SUCCESS, {})
  }
}

export class ForgotPassword extends SharedAction.FsaActionCreator<Types.AUTH_TROUBLE_FORGOT_PASSWORD, { email: string }> {
  constructor(email: string) {
    super(Types.AUTH_TROUBLE_FORGOT_PASSWORD, {email})
  }
}

export class ForgotPasswordSuccess extends SharedAction.FsaActionCreator<Types.AUTH_TROUBLE_FORGOT_PASSWORD_SUCCESS> {
  constructor() {
    super(Types.AUTH_TROUBLE_FORGOT_PASSWORD_SUCCESS, {})
  }
}


export class ChangePassword extends SharedAction.FsaActionCreator<Types.AUTH_CHANGE_PASSWORD, { password: string }> {
  constructor(password: string) {
    super(Types.AUTH_CHANGE_PASSWORD, {password});
  }
}

export class ChangePasswordSuccess extends SharedAction.FsaActionCreator<Types.AUTH_CHANGE_PASSWORD_SUCCESS> {
  constructor() {
    super(Types.AUTH_CHANGE_PASSWORD_SUCCESS, {});
  }
}

export class Error extends SharedAction.FsaActionCreator<Types.AUTH_ERROR, { errorMsg: string }> {

  constructor(errorMsg: string) {
    super(Types.AUTH_ERROR, {errorMsg}, true);
  }
}

export type Actions = SignUp
  | SignIn
  | SignOut
  | SignInSuccess
  | SignOutSuccess
  | ForgotPassword
  | ForgotPasswordSuccess
  | Error

export enum Types {
  AUTH_SIGN_UP = 'AUTH_SIGN_UP',
  AUTH_SIGN_IN = 'AUTH_SIGN_IN',
  AUTH_SIGN_IN_SUCCESS = 'AUTH_SIGN_IN_SUCCESS',
  AUTH_SIGN_OUT = 'AUTH_SIGN_OUT',
  AUTH_SIGN_OUT_SUCCESS = 'AUTH_SIGN_OUT_SUCCESS',
  AUTH_CHANGE_PASSWORD = 'AUTH_CHANGE_PASSWORD',
  AUTH_CHANGE_PASSWORD_SUCCESS = 'AUTH_CHANGE_PASSWORD_SUCCESS',
  AUTH_TROUBLE_FORGOT_PASSWORD = 'AUTH_TROUBLE_FORGOT_PASSWORD',
  AUTH_TROUBLE_FORGOT_PASSWORD_SUCCESS = 'AUTH_TROUBLE_FORGOT_PASSWORD_SUCCESS',
  AUTH_ERROR = 'AUTH_ERROR',
}
