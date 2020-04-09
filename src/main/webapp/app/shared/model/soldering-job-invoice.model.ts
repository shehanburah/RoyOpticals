import { Moment } from 'moment';

export interface ISolderingJobInvoice {
  id?: string;
  customerId?: string;
  price?: number;
  orderDate?: Moment;
  delivered?: boolean;
  deliveryDate?: Moment;
}

export class SolderingJobInvoice implements ISolderingJobInvoice {
  constructor(
    public id?: string,
    public customerId?: string,
    public price?: number,
    public orderDate?: Moment,
    public delivered?: boolean,
    public deliveryDate?: Moment
  ) {
    this.delivered = this.delivered || false;
  }
}
