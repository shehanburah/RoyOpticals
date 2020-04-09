import { Moment } from 'moment';

export interface IInvoice {
  id?: string;
  customerId?: string;
  rx?: number;
  deleveryDate?: Moment;
  orderDate?: Moment;
  advance?: number;
  balance?: number;
  delivered?: boolean;
  paid?: boolean;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: string,
    public customerId?: string,
    public rx?: number,
    public deleveryDate?: Moment,
    public orderDate?: Moment,
    public advance?: number,
    public balance?: number,
    public delivered?: boolean,
    public paid?: boolean
  ) {
    this.delivered = this.delivered || false;
    this.paid = this.paid || false;
  }
}
