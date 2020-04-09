import { Moment } from 'moment';

export interface IPettyCashStock {
  id?: string;
  itemName?: string;
  brandName?: string;
  modelNumber?: string;
  type?: string;
  size?: number;
  remarks?: string;
  quantity?: number;
  price?: number;
  discountedPrice?: number;
  stockInDate?: Moment;
  estNextStockInDate?: Moment;
}

export class PettyCashStock implements IPettyCashStock {
  constructor(
    public id?: string,
    public itemName?: string,
    public brandName?: string,
    public modelNumber?: string,
    public type?: string,
    public size?: number,
    public remarks?: string,
    public quantity?: number,
    public price?: number,
    public discountedPrice?: number,
    public stockInDate?: Moment,
    public estNextStockInDate?: Moment
  ) {}
}
