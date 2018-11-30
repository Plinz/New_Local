import { IImage } from 'app/shared/model//image.model';
import { ICategory } from 'app/shared/model//category.model';

export interface ICategory {
    id?: number;
    name?: string;
    description?: string;
    image?: IImage;
    categoryParent?: ICategory;
}

export class Category implements ICategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public image?: IImage,
        public categoryParent?: ICategory
    ) {}
}
