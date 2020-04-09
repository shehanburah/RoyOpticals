import { Moment } from 'moment';
import { Title } from 'app/shared/model/enumerations/title.model';

export interface ICustomer {
  id?: string;
  title?: Title;
  firstName?: string;
  lastName?: string;
  dob?: Moment;
  city?: string;
  address?: string;
  mobile?: string;
  landline?: string;
  gender?: boolean;
  registered?: Moment;
}

export class Customer implements ICustomer {
  constructor(
    public id?: string,
    public title?: Title,
    public firstName?: string,
    public lastName?: string,
    public dob?: Moment,
    public city?: string,
    public address?: string,
    public mobile?: string,
    public landline?: string,
    public gender?: boolean,
    public registered?: Moment
  ) {
    this.gender = this.gender || false;
  }
}
