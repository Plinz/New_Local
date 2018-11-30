import { IImage } from 'app/shared/model//image.model';
import { ICategory } from 'app/shared/model//category.model';

export interface IProductType {
    id?: number;
    name?: string;
    description?: string;
    image?: IImage;
    category?: ICategory;
}

export class ProductType implements IProductType {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public image?: IImage,
        public category?: ICategory
    ) {}
}
