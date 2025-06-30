import { cva, type VariantProps } from 'class-variance-authority'

export { default as Badge } from './Badge.vue'

export const badgeVariants = cva(
  'inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&>svg]:size-3 gap-1 [&>svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden',
  {
    variants: {
      variant: {
        default: 'bg-gray-100 text-gray-800 border-transparent',
        secondary: 'bg-gray-200 text-gray-800 border-transparent',
        destructive: 'bg-red-100 text-red-800 border-red-200',
        outline: 'border border-gray-300 text-gray-800 bg-transparent',
        success: 'bg-green-100 text-green-800 border-green-200',
        info: 'bg-blue-100 text-blue-800 border-blue-200',
        warning: 'bg-yellow-100 text-yellow-800 border-yellow-200',
      },
    },
    defaultVariants: {
      variant: 'default',
    },
  }
)
export type BadgeVariants = VariantProps<typeof badgeVariants>
