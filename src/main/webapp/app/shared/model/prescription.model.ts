import { Moment } from 'moment';

export interface IPrescription {
  id?: string;
  customerId?: string;
  invoiceId?: string;
  name?: string;
  date?: Moment;
  description?: any;
}

export class Prescription implements IPrescription {
  constructor(
    public id?: string,
    public customerId?: string,
    public invoiceId?: string,
    public name?: string,
    public date?: Moment,
    public description?: any
  ) {}
}
