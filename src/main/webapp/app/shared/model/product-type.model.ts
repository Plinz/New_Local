import { ICategory } from 'app/shared/model//category.model';

export interface IProductType {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
    category?: ICategory;
}

export class ProductType implements IProductType {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public category?: ICategory
    ) {}
}
